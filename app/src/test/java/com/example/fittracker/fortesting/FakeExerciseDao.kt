import com.example.fittracker.data.local.dao.ExerciseDao
import com.example.fittracker.data.local.dao.WorkoutLogDao
import com.example.fittracker.data.local.entity.ExerciseEntity
import com.example.fittracker.data.local.entity.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeExerciseDao : ExerciseDao {
    private val exercisesFlow = MutableStateFlow<List<ExerciseEntity>>(emptyList())
    override suspend fun insert(exercise: ExerciseEntity): Long {
        TODO("Not yet implemented")
    }


    override fun getAll(): Flow<List<ExerciseEntity>> = exercisesFlow
    override suspend fun count(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Long): ExerciseEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    fun setExercises(exercises: List<ExerciseEntity>) {
        exercisesFlow.value = exercises
    }
}

class FakeWorkoutLogDao : WorkoutLogDao {
    private val logsFlow = MutableStateFlow<List<WorkoutLogEntity>>(emptyList())
    override suspend fun insert(log: WorkoutLogEntity): Long {
        TODO("Not yet implemented")
    }

    override fun getLogsByExercise(exerciseId: Long): Flow<List<WorkoutLogEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLogsByDate(date: Long): List<WorkoutLogEntity> {
        TODO("Not yet implemented")
    }

    override fun getLogsByDateRange(
        start: Long,
        end: Long
    ): Flow<List<WorkoutLogEntity>> {
        TODO("Not yet implemented")
    }

    override fun getAllLogs(): Flow<List<WorkoutLogEntity>> = logsFlow
    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    fun setLogs(logs: List<WorkoutLogEntity>) {
        logsFlow.value = logs
    }
}