package com.iljin.apiServer.ijeas.system.vendor;

import java.math.BigDecimal;

public interface VendorRepositoryCustom {
    BigDecimal getPrepayCount(Integer vendorId, Integer vendorSiteId, String getPrepayCount);
}
