package com.ekoapp.sample.utils

fun getCurrentClassAndMethodNames(): String {
    val e = Thread.currentThread().stackTrace[3]
    val s = e.className
    return "<tags>" + s.substring(s.lastIndexOf('.') + 1, s.length) + "." + e.methodName
}