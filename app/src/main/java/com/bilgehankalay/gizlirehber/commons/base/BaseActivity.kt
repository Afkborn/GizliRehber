package com.bilgehankalay.gizlirehber.commons.base

import androidx.appcompat.app.AppCompatActivity
import com.bilgehankalay.gizlirehber.commons.events.SingleLiveEvent
import com.bilgehankalay.gizlirehber.commons.events.UiEvent

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Event that can be received in every activity that extends [BaseActivity]
     */
    val uiEvent = SingleLiveEvent<UiEvent>()

}