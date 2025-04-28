import logic.entity.User

fun main() {

    val activeUser: User

    println("Welcome to PlanMate v1.0\n")
    println("please login")

    print("userName: ")
    val userName = readln()
    print("password: "); readln()

    activeUser = User(userName = userName, type = User.Type.ADMIN)
    println("\n-------------------------------------")
    println("Welcome ${activeUser.userName}\n")
    println("choose one of the following options: \n")
    println("1. View all projects")
    println("2. Manage Mates")
    println("0. Exit the App")
    print("Your Input: ")
    val userInput = readln().also { if (it == "0") return }
    println("\n-------------------------------------")
    println("Projects")

}