package com.ekoapp.sample.core.utils

fun getCurrentClassAndMethodNames(): String {
    val e = Thread.currentThread().stackTrace[3]
    val s = e.className
    return "<where> " + s.substring(s.lastIndexOf('.') + 1, s.length) + "." + e.methodName
}