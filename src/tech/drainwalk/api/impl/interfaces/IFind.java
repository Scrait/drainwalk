package tech.drainwalk.api.impl.interfaces;

import by.radioegor146.nativeobfuscator.NotNative;

public interface IFind<V> {

    @NotNative
    <T extends V> T findByName(final String name);

    @NotNative
    <T extends V> T findByClass(final Class<T> clazz);

}
