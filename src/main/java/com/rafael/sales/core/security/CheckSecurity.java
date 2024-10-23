package com.rafael.sales.core.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

    public @interface Products {

        @Retention(RUNTIME)
        @Target(METHOD)
        @PreAuthorize("hasAuthority('SCOPE_READ')")
        public @interface canRead { }

        @Retention(RUNTIME)
        @Target(METHOD)
        @PreAuthorize("hasAuthority('SCOPE_WRITE')")
        public @interface canWrite { }

    }

}
