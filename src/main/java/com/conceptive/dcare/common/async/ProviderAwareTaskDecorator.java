package com.conceptive.dcare.common.async;

import com.conceptive.dcare.common.model.ProviderContext;
import org.springframework.core.task.TaskDecorator;

public class ProviderAwareTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        String providerId= ProviderContext.getProviderId();
        return ()->{
            try {
                ProviderContext.setProviderId(providerId);
                runnable.run();
            }finally {
                ProviderContext.setProviderId(null);
            }
        };

    }
}
