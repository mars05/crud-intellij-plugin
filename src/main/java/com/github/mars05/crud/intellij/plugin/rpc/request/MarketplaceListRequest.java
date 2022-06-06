package com.github.mars05.crud.intellij.plugin.rpc.request;

import com.github.mars05.crud.intellij.plugin.rpc.response.MarketplaceListResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xiaoyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MarketplaceListRequest extends Request<MarketplaceListResponse> {
    private String keyword;

    private Integer projectType;

    private String organizationName;

    private String createName;

    private Integer pageNumber = 1;
    private Integer pageSize = 20;

    @Override
    public String getPath() {
        return "/api/crud-hub/marketplace/list";
    }

    @Override
    public Class<MarketplaceListResponse> getResponseClass() {
        return MarketplaceListResponse.class;
    }
}
