package fi.tuni.finalprojectandroid

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddUserDialog(onAddUser: (User) -> Unit, onDismiss: () -> Unit, sortedUsers: List<User>, setUsers: (List<User>) -> Unit) {
    var context = LocalContext.current
    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val mainBackground = Color(android.graphics.Color.parseColor("#242424"))

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp),
            elevation = 8.dp,
            color = Color.LightGray
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            ) {
                Text(
                    text = "Add User",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { newValue -> if (newValue.all { it.isDigit() }) { age = newValue}  },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { if (firstName.isBlank() || lastName.isBlank() || age.isBlank() || email.isBlank() || phone.isBlank()) {
                            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()

                            } else {
                                val newUser = User(
                                    id = id,
                                    firstName = firstName,
                                    lastName = lastName,
                                    age = age.toInt(),
                                    email = email,
                                    phone = phone,
                                    image = "https://robohash.org/${firstName + lastName}"
                                )
                                AddUser(newUser, sortedUsers, setUsers)
                                onAddUser(newUser)
                                onDismiss()
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}