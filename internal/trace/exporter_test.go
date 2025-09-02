// Copyright (c) 2025 Bytedance Ltd. and/or its affiliates
// SPDX-License-Identifier: MIT

package trace

import (
	"context"
	"testing"

	. "github.com/bytedance/mockey"
	"github.com/coze-dev/cozeloop-go/entity"
	"github.com/coze-dev/cozeloop-go/internal/httpclient"
	. "github.com/smartystreets/goconvey/convey"
)

func Test_ExportSpans(t *testing.T) {
	ctx := context.Background()
	spans := []*entity.UploadSpan{&entity.UploadSpan{}, &entity.UploadSpan{}}

	PatchConvey("Test ExportSpans", t, func() {
		// 创建mock的httpclient
		mockClient := &httpclient.Client{}
		Mock(mockClient.Post).Return(nil).Build()
		
		// 创建SpanExporter实例
		exporter := &SpanExporter{
			client: mockClient,
			uploadPath: UploadPath{
				spanUploadPath: "/test/path",
				fileUploadPath: "/test/file",
			},
		}
		
		err := exporter.ExportSpans(ctx, spans)
		So(err, ShouldBeNil)
	})
}
