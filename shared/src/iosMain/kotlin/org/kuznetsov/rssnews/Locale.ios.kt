package org.kuznetsov.rssnews

import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleCountryCode
import platform.Foundation.currentLocale

actual fun currentRegion(): String = (NSLocale.currentLocale.objectForKey(NSLocaleCountryCode) as? String).orEmpty()
