package com.mada.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("id") val id: Int,
    @SerializedName("date") var date: String,
    @SerializedName("category") var category: Category,
    @SerializedName("todoName") var todoName: String,
    @SerializedName("complete") var complete: Boolean,
    @SerializedName("repeat") var repeat: String,
    @SerializedName("repeatInfo") var repeatInfo : Int? = null,
    @SerializedName("startRepeatDate") var startRepeatDate: String? = null,
    @SerializedName("endRepeatDate") var endRepeatDate: String? = null
)

data class RepeatTodo(
    @SerializedName("id") val id: Int,
    @SerializedName("todoId") val todoId : Int,
    @SerializedName("date") var date: String,
    @SerializedName("todoName") var todoName: String,
    @SerializedName("category") var category : Category,
    @SerializedName("complete") var complete: Boolean
)

data class todoData(
    @SerializedName("Todo") val Todo : Todo
)

data class todoData2(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("TodoList") val TodoList : ArrayList<Todo>,
    @SerializedName("RepeatTodoList") val RepeatTodoList : ArrayList<GetRepeatTodo>
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
    @SerializedName("repeatInfo") var repeatInfo : Int?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?
)

data class PatchRequestTodo(
    @SerializedName("todoName") var todoName: String,
    @SerializedName("repeat") var repeat: String?,
    @SerializedName("repeatInfo") var repeatInfo: Int?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?,
    @SerializedName("complete") var complete : Boolean,
    @SerializedName("date") var date: String?
)
data class GetRepeatTodo(
    @SerializedName("id") val id: Int,
    @SerializedName("todoId") val todoId : Int,
    @SerializedName("repeatTodoName") val repeatTodoName : String,
    @SerializedName("categoryId") val categoryId : Int,
    @SerializedName("date") var date: String,
    @SerializedName("complete") var complete : Boolean
)




