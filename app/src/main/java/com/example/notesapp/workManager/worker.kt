package com.example.notesapp.workManager

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notesapp.room.NotesDao
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class worker(private val context: Context, params: WorkerParameters) : Worker(context, params) {


    override fun doWork(): Result {

        try {
            Log.d(TAG, "Worker Run")

           // for (i in 1..100) {
             //   Log.d(TAG, "Uploading $i")
           // }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()

        }

    }
}
