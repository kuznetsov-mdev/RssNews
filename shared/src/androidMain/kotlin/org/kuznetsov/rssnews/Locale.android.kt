package org.kuznetsov.rssnews

import java.util.Locale

actual fun currentRegion(): String = Locale.getDefault().country
