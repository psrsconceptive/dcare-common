package com.conceptive.dcare.common.model;

public class ProviderContext {

    private static  ThreadLocal<String> providerIdThredLocal = new ThreadLocal<>();

   public static String getProviderId(){
       return providerIdThredLocal.get();
   }

    public static void setProviderId(String providerId) {
        providerIdThredLocal.set(providerId);
    }
}
