@file:Suppress("unused")

package pfhb.damian.kotlinlearning___

interface GetDefault {
    fun getDefault() = "Default"
    fun getExitCode(code : String) = "Exit Code: $code"
    fun getId(id : String) = "Identity: $id"
}

data class MainDataModelContainer(var container: List<MainDataModel>)
data class CreditDataContainer(var container : List<CreditData>)

data class MainDataModel(private var name: String, private var cash: Float, private var password: String, private var pin: Int) : GetDefault{
    private val identity : String = "${(10..99).random()}-${(1000..9999).random()}-${(1000..9999).random()}"

    fun default() : String{
        return getDefault()
    }
    fun getIdentity() : String{
        return getId(identity)
    }
    fun getData() : String{
        return "Identity: $identity Name: $name Cash: ${"%.2f".format(cash)}$"
    }

    fun getCash() : Float{
        return cash
    }
    fun changeCash(amount : Float, pwd : String){
        if(checkForPassword(pwd))
            cash += amount
    }

    fun checkForPassword(pwd : String) : Boolean{
        return password == pwd
    }

    fun checkForPin(pin : Int) : Boolean{
        return this.pin == pin
    }

    fun getName() : String {
        return name
    }

    fun payInterest(percent : Float) : Float{
        cash *= 1+percent/100
        return cash
    }
}

data class CreditData(val user : MainDataModel, private val loanSum : Float, private val  loanMonths: Int, private val loanInterest : Float) {
    fun getData() : String{
        return "${user.getName()} || Sum: ${"%.2f".format(loanSum)}$ Months: $loanMonths Interest: $loanInterest% \n"
    }
}

enum class ExitCode{
    OK,
    ERROR_BAD_PASSWORD,
    ERROR_INTERNAL_PROBLEM,
    ERROR_INSUFFICIENT_AMOUNT_OF_CASH
}

class Manager : GetDefault {

    private var listOfUsers = arrayListOf<MainDataModel>()
    private var listOfCredits = arrayListOf<CreditData>()


    private var exitCode = ExitCode.OK

    /*///////////////////////////////////////////////////////////////////////////////
    *
    *      PRIVATE FUNCTIONS HERE             //////////////////////////////////////
    *
    *///////////////////////////////////////////////////////////////////////////////
    private fun getListOfUsers() : ArrayList<MainDataModel>{
        return listOfUsers
    }
    private fun setListOfUsers(users : ArrayList<MainDataModel>){
        this.listOfUsers = users
    }
    private fun addUserToListOfUsers(user : MainDataModel){
        this.listOfUsers.add(user)
    }

    private fun getCreditInfo(credit : CreditData) :String{
        return credit.getData()
    }

    private fun getUserToString(user : MainDataModel) : String{
        val model : MainDataModel = user
        return model.getData()
    }

    /*///////////////////////////////////////////////////////////////////////////////
    *
    *      PUBLIC FUNCTIONS HERE             //////////////////////////////////////
    *
    *///////////////////////////////////////////////////////////////////////////////

    fun getExitCode(): String {
        return super.getExitCode(exitCode.toString())
    }

    fun newCredit(user : MainDataModel, loanSum : Float, loanMonths: Int, loanInterest: Float){
        val cd = CreditData(user, loanSum, loanMonths, loanInterest)
        listOfCredits.add(cd)
    }


    fun getUserCredits(user : MainDataModel) : List<CreditData>{
        val list = arrayListOf<CreditData>()
        for(credit in listOfCredits){
            if(credit.user == user){
                list.add(credit)
            }
        }
        return list
    }

    fun getCreditInfoFromList(listOfCredits : List<CreditData>): String{
        var info = "\nCredit Information:\n\n"
        for(list in listOfCredits){
            info += getCreditInfo(list)
        }
        return info
    }


    fun newUser(name: String, cash: Float, password: String, pin : Int) : MainDataModel{
        val mdm = MainDataModel(name, cash, password, pin)
        addUserToListOfUsers(mdm)
        return mdm
    }


    fun makeTransaction(sender: MainDataModel, receiver: MainDataModel, pwd_sender : String, amount: Float) : Boolean {
        if(sender.checkForPassword(pwd_sender)){
            if(sender.getCash() > amount){
                sender.changeCash(-amount, pwd_sender)
                receiver.changeCash(amount, pwd_sender)
            }
            else {
                exitCode = ExitCode.ERROR_INSUFFICIENT_AMOUNT_OF_CASH
                return false
            }
        }else {
            exitCode = ExitCode.ERROR_BAD_PASSWORD
            return false
        }
        exitCode = ExitCode.OK
        return true
    }

    fun transactionPayOut(user : MainDataModel, pwd_user : String, amount: Float) : Boolean{
        if(user.checkForPassword(pwd_user)){
            if(user.getCash() > amount) {
                user.changeCash(amount, pwd_user)
            }
            else {
                exitCode = ExitCode.ERROR_INSUFFICIENT_AMOUNT_OF_CASH
                return false
            }
        }
        else {
            exitCode = ExitCode.ERROR_BAD_PASSWORD
            return false
        }
        exitCode = ExitCode.OK
        return true
    }


    fun getAllUsersToString() : String{
        var result = ""
        for (user in getListOfUsers()){
            result += "${getUserToString(user)}\n"
        }
        return when {
            result != "" -> result
            else -> "No data"
        }
    }

    fun payInterest(user : MainDataModel, percent : Float) : Float{
        return user.payInterest(percent)
    }

    fun getContainerOfData() : MainDataModelContainer{
        return MainDataModelContainer(getListOfUsers())
    }
    fun loadDataFromContainerOfData(container : MainDataModelContainer){
        for(user in container.container)
            listOfUsers.add(user)
    }

    fun getContainerOfCredits() : CreditDataContainer{
        return CreditDataContainer(listOfCredits)
    }
    fun loadDataFromContainerOfCredits(container : CreditDataContainer){
        for(user in container.container)
            listOfCredits.add(user)
    }`

    fun getUserByName(name : String) : MainDataModel? {
        for(users in getListOfUsers()){
            if(users.getName() == name){
                return users
            }
        }
        return null
    }

    fun getSizeOfUsers() : Int{
        return listOfUsers.size
    }

    fun clearData(){
        listOfUsers.removeAll(listOfUsers)
    }

}