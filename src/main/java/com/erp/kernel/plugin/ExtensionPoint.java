package com.erp.kernel.plugin;

/**
 * Defines a named extension point that plugins can contribute to.
 *
 * <p>Extension points allow the kernel to declare hook locations where
 * plugins can register custom behaviour. Each extension point has a unique
 * name and accepts contributions of a specific type.
 *
 * @param <T> the type of extension contributions
 */
public interface ExtensionPoint<T> {

    /**
     * Returns the unique name of this extension point.
     *
     * @return the extension point name
     */
    String getName();

    /**
     * Returns the type of contributions accepted by this extension point.
     *
     * @return the contribution class
     */
    Class<T> getType();
}
