package com.mada.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("id") val id: Int,
    @SerializedName("date") var date: String,
    @SerializedName("category") var category: Category,
    @SerializedName("todoName") var todoName: String,
    @SerializedName("complete") var complete: Boolean,
    @SerializedName("repeat") var repeat: String,
    @SerializedName("repeatWeek") var repeatWeek: String?,
    @SerializedName("repeatMonth") var repeatMonth: String?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?,
    @SerializedName("isAlarm") var isAlarm: Boolean,
    @SerializedName("startTodoAtMonday") var startTodoAtMonday: Boolean,
    @SerializedName("endTodoBackSetting") var endTodoBackSetting: Boolean,
    @SerializedName("newTodoStartSetting") var newTodoStartSetting: Boolean
)

data class todoData(
    @SerializedName("Todo") val Todo : Todo
)

data class todoData2(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("TodoList") val TodoList : ArrayList<Todo>
)

data class TodoList(
    @SerializedName("data") var data: todoData2
)

data class PostResponseTodo(
    @SerializedName("data") val data : todoData
)

data class PostRequestTodoCateId(
    @SerializedName("id") val id: Int
)

data class PostRequestTodo(
    @SerializedName("date") var date: String?,
    @SerializedName("category") var category: PostRequestTodoCateId,
    @SerializedName("todoName") var todoName: String,
    @SerializedName("complete") var complete: Boolean,
    @SerializedName("repeat") var repeat: String,
    @SerializedName("repeatWeek") var repeatWeek: String?,
    @SerializedName("repeatMonth") var repeatMonth: String?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?,
    @SerializedName("isAlarm") var isAlarm: Boolean,
    @SerializedName("startTodoAtMonday") var startTodoAtMonday: Boolean,
    @SerializedName("endTodoBackSetting") var endTodoBackSetting: Boolean,
    @SerializedName("newTodoStartSetting") var newTodoStartSetting: Boolean

)

data class PatchRequestTodo(
    @SerializedName("todoName") var todoName: String,
    @SerializedName("repeat") var repeat: String,
    @SerializedName("repeatWeek") var repeatWeek: String?,
    @SerializedName("repeatMonth") var repeatMonth: String?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?,
    @SerializedName("complete") var complete : Boolean,
    @SerializedName("date") var date: String?
)




