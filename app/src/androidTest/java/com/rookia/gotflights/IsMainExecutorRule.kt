package com.rookia.gotflights

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.DefaultTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Transactions in room use runBlocking somewhere inside and that cause deadlock when testing with
 * InstantTaskExecutorRule.
 * Replacing that rule by this one fixes the problem, as explained in
 * https://stackoverflow.com/questions/57027850/testing-android-room-with-livedata-coroutines-and-transactions
 */
class IsMainExecutorRule : TestWatcher() {

    val defaultExecutor = DefaultTaskExecutor()

    override fun starting(description: Description?) {
        super.starting(description)
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                defaultExecutor.executeOnDiskIO(runnable)
            }

            override fun postToMainThread(runnable: Runnable) {
                defaultExecutor.executeOnDiskIO(runnable)
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    override fun finished(description: Description?) {
        super.finished(description)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}