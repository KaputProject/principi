import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.Header
import ui.Sidebar
import ui.components.colors.AppTheme
import ui.dataClasses.account.Account
import ui.dataClasses.locations.Location
import ui.dataClasses.user.User
import ui.pages.accountPages.*
import ui.pages.locationPages.*
import ui.pages.userPages.*
import ui.pages.*

@Composable
fun App() {
    var currentPage by remember { mutableStateOf(1) }
    var userToEdit by remember { mutableStateOf<User?>(null) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var selectedAccount by remember { mutableStateOf<Account?>(null) }
    var accountToEdit by remember { mutableStateOf<Account?>(null) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var locationToEdit by remember { mutableStateOf<Location?>(null) }

    // Dodaj možnost preklopa teme (lahko tudi po sistemu)
    var darkModeEnabled by remember { mutableStateOf(false) }
    var darkTheme by remember { mutableStateOf(false) } // <- novo stanje

    AppTheme(darkTheme = darkModeEnabled) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Header(
                darkTheme = darkModeEnabled,
                onToggleTheme = { darkModeEnabled = !darkModeEnabled }
            )

            Row(Modifier.weight(1f)) {
                Sidebar(currentPage = currentPage, onNavigate = { page ->
                    currentPage = page
                })

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                        .padding(8.dp)
                ) {
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
                        } ?: Text("Napaka: uporabnik ni izbran.", color = MaterialTheme.colors.error)

                        7 -> userToEdit?.let {
                            UserEdit(
                                initialUser = it,
                                onBackClick = { currentPage = 8 }
                            )
                        } ?: Text("No user selected", color = MaterialTheme.colors.error)

                        8 -> selectedUser?.let {
                            UserMenu(
                                user = it,
                                onEditClick = { user ->
                                    userToEdit = user
                                    currentPage = 7
                                },
                                onAccountClick = {
                                    currentPage = 9
                                },
                                onLocationClick = {
                                    currentPage = 12
                                },
                                onBackClick = { currentPage = 2 }
                            )
                        } ?: Text("Napaka: uporabnik ni izbran.", color = MaterialTheme.colors.error)

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
                        } ?: Text("Napaka: uporabnik ni izbran.", color = MaterialTheme.colors.error)

                        10 -> selectedAccount?.let {
                            ShowAccount(
                                account = it,
                                onBackClick = { currentPage = 9 },
                                onEditClick = { account ->
                                    accountToEdit = account
                                    currentPage = 11
                                },
                            )
                        } ?: Text("Napaka: račun ni izbran.", color = MaterialTheme.colors.error)

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
                                        currentPage = 9
                                    }
                                )
                            } ?: Text("Napaka: uporabnik ni izbran.", color = MaterialTheme.colors.error)
                        } ?: Text("Napaka: račun za urejanje ni izbran.", color = MaterialTheme.colors.error)

                        12 -> selectedUser?.let { user ->
                            Locations(
                                initialUser = user,
                                onBackClick = { currentPage = 8 },
                                onNavigate = { location ->
                                    selectedLocation = location
                                    currentPage = 13
                                },
                                onCreateClick = { userForCreate ->
                                    selectedUser = userForCreate
                                    currentPage = 15
                                }

                            )
                        } ?: Text("Napaka: uporabnik ni izbran.", color = MaterialTheme.colors.error)

                        13 -> selectedLocation?.let { location ->
                            ShowLocation(
                                location = location,
                                onBackClick = { currentPage = 12 },
                                onEditClick = { loc ->
                                    selectedLocation = loc
                                    locationToEdit = loc
                                    currentPage = 14
                                }
                            )
                        } ?: Text("Napaka: lokacija ni izbrana.", color = MaterialTheme.colors.error)

                        14 -> locationToEdit?.let { location ->
                            selectedUser?.let { user ->
                                LocationEdit(
                                    initialLocation = location,
                                    user = user,
                                    onBackClick = { currentPage = 13 },
                                    onLocationUpdated = { updatedLocation ->
                                        selectedLocation = updatedLocation
                                        locationToEdit = updatedLocation
                                        currentPage = 13
                                    },
                                    onLocationDeleted = {
                                        selectedLocation = null
                                        locationToEdit = null
                                        currentPage = 12
                                    }
                                )
                            } ?: Text("Napaka: uporabnik ni izbran.", color = MaterialTheme.colors.error)
                        } ?: Text("Napaka: lokacija za urejanje ni izbran.", color = MaterialTheme.colors.error)

                        15 -> selectedUser?.let { user ->
                            LocationCreate(
                                user = user,
                                onBackClick = { currentPage = 12 },
                                onLocationCreated = { newLocation ->
                                    selectedLocation = newLocation
                                    currentPage = 13
                                }
                            )
                        } ?: Text("Napaka: uporabnik ni izbran.", color = MaterialTheme.colors.error)

                    }
                }
            }
        }
    }
}
