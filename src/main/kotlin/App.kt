import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.Header
import ui.pages.*
import ui.Sidebar
import ui.dataClasses.account.Account
import ui.dataClasses.user.User
import ui.pages.accountPages.AccountCreate
import ui.pages.accountPages.Accounts
import ui.pages.accountPages.ShowAccount
import ui.pages.accountPages.AccountEdit
import ui.pages.userPages.UserCreate
import ui.pages.userPages.UserEditPage
import ui.pages.userPages.UserMenuPage
import ui.pages.userPages.Users

@Composable
fun App() {
    var currentPage by remember { mutableStateOf(1) }
    var userToEdit by remember { mutableStateOf<User?>(null) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var selectedAccount by remember { mutableStateOf<Account?>(null) }
    var accountToEdit by remember { mutableStateOf<Account?>(null) }
    var currentUser by remember { mutableStateOf<User?>(null) }

    MaterialTheme {
        Column(Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
            Header()

            Row(Modifier.weight(1f)) {
                Sidebar(currentPage = currentPage, onNavigate = { page ->
                    currentPage = page
                })

                Box(Modifier.fillMaxSize().background(Color.White).padding(8.dp)) {
                    when (currentPage) {
                        1 -> UserCreate()
                        2 -> Users(onNavigate = { clickedUser ->
                            selectedUser = clickedUser
                            currentPage = 8
                        })
                        3 -> Scraper()
                        4 -> Generator()
                        6 -> selectedUser?.let {
                            AccountCreate(
                                user = it,
                                onBackClick = { currentPage = 9 }
                            )
                        } ?: Text("Napaka: uporabnik ni izbran.")



                        7 -> userToEdit?.let { UserEditPage(
                            initialUser = it,
                            onBackClick = { currentPage = 8 }
                        ) } ?: Text("No user selected")
                        8 -> {
                            selectedUser?.let {
                                UserMenuPage(
                                    user = it,
                                    onEditClick = { user ->
                                        userToEdit = user
                                        currentPage = 7
                                    },
                                    onAccountClick = {
                                        currentPage = 9
                                    },
                                    onBackClick = { currentPage = 2 }
                                )
                            } ?: Text("Napaka: uporabnik ni izbran.")
                        }
                        9 -> selectedUser?.let { user ->
                            Accounts(
                                initialUser = user,
                                onBackClick = { currentPage = 8 },
                                onNavigate = { account ->
                                    selectedAccount = account
                                    currentPage = 10
                                },
                                onCreateClick = { userForCreate ->
                                    selectedUser = userForCreate
                                    currentPage = 6
                                }
                            )
                        } ?: Text("Napaka: uporabnik ni izbran.")


                        10 -> selectedAccount?.let {
                            ShowAccount(
                                account = it,
                                onBackClick = { currentPage = 9 },
                                onEditClick = { account ->
                                    accountToEdit = account
                                    currentPage = 11
                                },
                            )
                        } ?: Text("Napaka: račun ni izbran.")
                        11 -> accountToEdit?.let { account ->
                            selectedUser?.let { user ->
                                AccountEdit(
                                    user = user,
                                    initialAccount = account,
                                    onBackClick = { currentPage = 10 },
                                    onAccountUpdated = { updatedAccount ->
                                        selectedAccount = updatedAccount
                                        accountToEdit = updatedAccount
                                    },
                                    onAccountDeleted = {
                                        selectedAccount = null
                                        accountToEdit = null
                                        currentPage = 9 // <- po brisanju nazaj na seznam računov
                                    }
                                )

                            } ?: Text("Napaka: uporabnik ni izbran.")
                        } ?: Text("Napaka: račun za urejanje ni izbran.")
                    }
                }
            }
        }
    }
}



