package com.example.pedro.myapplication.data.remote.exceptions

import java.lang.Exception
import java.lang.RuntimeException

class CryptopiaException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
