package com.iljin.apiServer.core.audit;

import com.iljin.apiServer.core.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    //audit
    private final Util util;
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(util.getLoginId());
    }
}
