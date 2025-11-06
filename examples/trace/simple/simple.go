// Copyright (c) 2025 Bytedance Ltd. and/or its affiliates
// SPDX-License-Identifier: MIT

package main

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"time"

	"github.com/coze-dev/cozeloop-go"
	"github.com/coze-dev/cozeloop-go/internal/logger"
	"github.com/coze-dev/cozeloop-go/spec/tracespec"
)

type llmRunner struct {
	client cozeloop.Client
}

// OpenAI API request/response structures
type OpenAIMessage struct {
	Role    string        `json:"role"`
	Content []interface{} `json:"content"`
}

type TextContent struct {
	Type string `json:"type"`
	Text string `json:"text"`
}

type ImageContent struct {
	Type     string `json:"type"`
	ImageURL struct {
		URL string `json:"url"`
	} `json:"image_url"`
}

type OpenAIRequest struct {
	Model    string          `json:"model"`
	Messages []OpenAIMessage `json:"messages"`
}

type OpenAIChoice struct {
	Message struct {
		Role    string `json:"role"`
		Content string `json:"content"`
	} `json:"message"`
}

type OpenAIUsage struct {
	PromptTokens     int `json:"prompt_tokens"`
	CompletionTokens int `json:"completion_tokens"`
	TotalTokens      int `json:"total_tokens"`
}

type OpenAIResponse struct {
	Choices []OpenAIChoice `json:"choices"`
	Usage   OpenAIUsage    `json:"usage"`
}

const (
	errCodeLLMCall = 600789111
)

func main() {
	// Set the following environment variables first (Assuming you are using a PAT token.).
	// COZELOOP_WORKSPACE_ID=your workspace id
	// COZELOOP_API_TOKEN=your token
	// COZELOOP_API_BASE_URL=your base url
	os.Setenv("COZELOOP_WORKSPACE_ID", "7551727668153024513")
	os.Setenv("COZELOOP_API_TOKEN", "2a6954061ba505c293f64580293a340de57c3010154087b4e499416da5750976")
	os.Setenv("COZELOOP_API_BASE_URL", "http://innovate-loop.ttb.test.ke.com")

	// 0. new client span
	logger.SetLogLevel(logger.LogLevelInfo)
	client, err := cozeloop.NewClient()
	if err != nil {
		panic(err)
	}
	ctx := context.Background()

	llmRunner := llmRunner{
		client: client,
	}

	// 1. start span
	ctx, span := client.StartSpan(ctx, "root_span", "main_span", nil)

	// 2. span set tag or baggage
	// set custom tag
	span.SetTags(ctx, map[string]interface{}{
		"mode":                  "simple",
		"node_id":               6076665,
		"node_process_duration": 228.6,
		"is_first_node":         true,
	})

	// set custom baggage, baggage can cover tag of sample key, and baggage will pass to child span automatically.
	span.SetBaggage(ctx, map[string]string{
		"product_id": "123456654321", // Assuming product_id is global field
	})
	// set baggage key: `user_id`, implicitly set tag key: `user_id`
	span.SetUserIDBaggage(ctx, "123456")

	// assuming call llm
	if err = llmRunner.llmCall(ctx); err != nil {
		// set tag key: `_status_code`
		span.SetStatusCode(ctx, errCodeLLMCall)
		// set tag key: `error`, if `_status_code` value is not defined, `_status_code` value will be set -1.
		span.SetError(ctx, err)
	}

	// 3. span finish
	span.Finish(ctx)

	// 4. (optional) flush or close
	// -- force flush, report all traces in the queue
	// Warning! In general, this method is not needed to be call, as spans will be automatically reported in batches.
	// Note that flush will block and wait for the report to complete, and it may cause frequent reporting,
	// affecting performance.
	client.Flush(ctx)

	// -- close trace, do flush and close client
	// Warning! Once Close is executed, the client will become unavailable and a new client needs
	// to be created via NewClient! Use it only when you need to release resources, such as shutting down an instance!
	//client.Close(ctx)
}

func (r *llmRunner) llmCall(ctx context.Context) (err error) {
	ctx, span := r.client.StartSpan(ctx, "llmCall", tracespec.VModelSpanType, nil)
	defer span.Finish(ctx)

	fmt.Println("Starting OpenAI API call...")

	// Real OpenAI API call
	apiURL := "https://openapi-ait.ke.com/v1/chat/completions"
	apiKey := "c5959d3b-91d2-47f7-8d58-9516c6174cf3"
	modelName := "deepseek-chat"

	// Prepare the request payload - simple text only
	input := "你好"
	requestPayload := OpenAIRequest{
		Model: modelName,
		Messages: []OpenAIMessage{
			{
				Role: "user",
				Content: []interface{}{
					TextContent{
						Type: "text",
						Text: input,
					},
				},
			},
		},
	}

	// Convert to JSON
	jsonData, err := json.Marshal(requestPayload)
	if err != nil {
		fmt.Printf("Failed to marshal request: %v\n", err)
		return fmt.Errorf("failed to marshal request: %w", err)
	}
	fmt.Printf("Request payload prepared, size: %d bytes\n", len(jsonData))

	// Create HTTP request
	req, err := http.NewRequestWithContext(ctx, "POST", apiURL, bytes.NewBuffer(jsonData))
	if err != nil {
		fmt.Printf("Failed to create request: %v\n", err)
		return fmt.Errorf("failed to create request: %w", err)
	}

	// Set headers
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer "+apiKey)
	fmt.Println("Making HTTP request to OpenAI...")

	// Make the request
	client := &http.Client{
		Timeout: 30 * time.Second,
	}

	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("HTTP request failed: %v\n", err)
		return fmt.Errorf("failed to make request: %w", err)
	}
	defer resp.Body.Close()

	// Record first response time
	firstRespTime := time.Now()
	fmt.Printf("Received response with status: %d\n", resp.StatusCode)

	// Read response
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		fmt.Printf("Failed to read response body: %v\n", err)
		return fmt.Errorf("failed to read response: %w", err)
	}

	if resp.StatusCode != http.StatusOK {
		fmt.Printf("API error response: %s\n", string(body))
		return fmt.Errorf("API request failed with status %d: %s", resp.StatusCode, string(body))
	}

	// Parse response
	var openaiResp OpenAIResponse
	err = json.Unmarshal(body, &openaiResp)
	if err != nil {
		fmt.Printf("Failed to unmarshal response: %v\n", err)
		fmt.Printf("Response body: %s\n", string(body))
		return fmt.Errorf("failed to unmarshal response: %w", err)
	}

	// Extract response content
	var respChoices []string
	for _, choice := range openaiResp.Choices {
		respChoices = append(respChoices, choice.Message.Content)
	}

	// Print success info for verification
	fmt.Printf("OpenAI API call successful!\n")
	fmt.Printf("Input: %s\n", input)
	fmt.Printf("Output: %v\n", respChoices)
	fmt.Printf("Token usage - Prompt: %d, Completion: %d, Total: %d\n",
		openaiResp.Usage.PromptTokens, openaiResp.Usage.CompletionTokens, openaiResp.Usage.TotalTokens)

	// Set span tags with real data
	span.SetInput(ctx, input)
	span.SetOutput(ctx, respChoices)
	span.SetModelProvider(ctx, "openai")
	span.SetStartTimeFirstResp(ctx, firstRespTime.UnixMicro())
	span.SetInputTokens(ctx, openaiResp.Usage.PromptTokens)
	span.SetOutputTokens(ctx, openaiResp.Usage.CompletionTokens)
	span.SetModelName(ctx, modelName)

	fmt.Println("Span tags set successfully")
	return nil
}

type MyTransport struct {
	Header           http.Header
	DefaultTransport http.RoundTripper
}

func (transport *MyTransport) RoundTrip(req *http.Request) (*http.Response, error) {
	for key, values := range transport.Header {
		for _, value := range values {
			req.Header.Add(key, value)
		}
	}
	return transport.DefaultTransport.RoundTrip(req)
}
