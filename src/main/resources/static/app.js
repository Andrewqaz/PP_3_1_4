"use strict";

const requestUrl = 'http://localhost:8080/rest/admin'
const table = document.getElementById('tableAllUsers');
const editModal = new bootstrap.Modal(document.getElementById('edit'));
const deleteModal = new bootstrap.Modal(document.getElementById('delete'));
const editeForm = document.getElementById('modalEditForm');
const deleteForm = document.getElementById('modalDeleteForm');
const addUserForm = document.getElementById('addNewUserForm');
const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json; charset=UTF-8',
    'Referer': null
}

const getUser = async (url) => await fetch(url,
    {
        method: "GET",
        headers: headers
    });

const updateUser = async (user) => await fetch(requestUrl,
    {
        method: "PUT",
        headers: headers,
        body: JSON.stringify(user)
    });

const addUser = async (user) => await fetch(requestUrl,
    {
        method: "POST",
        headers: headers,
        body: JSON.stringify(user)
    });

const deleteUser = async (url) => await fetch(url,
    {
        method: "DELETE",
        headers: headers
    });

function sendRequest(url) {
    return fetch(url).then(response => {
        if (response.ok) {
            return response.json();
        }
        return response.json().then(err => {
            const e = new Error('Ошибка');
            e.data = err;
            throw e;
        })
    })
}

//заполнение таблицы юзеров
function fillUsersTable() {
    sendRequest(requestUrl).then(
        users => {
            if (users.length > 0) {
                let temp = ""
                users.forEach((user) => {
                        temp += "<tr id = \"userRow" + user.id + "\">";
                        temp += "<td>" + user.id + "</td>";
                        temp += "<td>" + user.firstName + "</td>";
                        temp += "<td>" + user.lastName + "</td>";
                        temp += "<td>" + user.age + "</td>";
                        temp += "<td>" + user.email + "</td>";
                        let roleString = "";
                        user.roles.forEach(r => {
                            roleString += r.name + " ";
                        })
                        temp += "<td>" + roleString + "</td>";
                        temp +=
                            "<td>\n" +
                            "<button id =\"buttonMoalEdit\" type=\"button\" class=\"btn btn-info\"\n " +
                            "data-target=\"#edit\">\n" +
                            " Edit\n" +
                            "</button>\n" +
                            "</td>\n";

                        temp +=
                            "<td>\n" +
                            "<button id =\"buttonMoalDelete\" type=\"button\" class=\"btn btn-danger\" " +
                            "data-target=\"#delete\">\n" +
                            "Delete\n" +
                            "</button>\n" +
                            "</td>"
                        temp += "</tr>";
                    }
                )
                document.getElementById("tableAllUsers").innerHTML = temp;
            }
        }
    )
}

fillUsersTable();

//модальные окна
table.addEventListener('click', (event) => {
    if (event.target.tagName !== 'BUTTON') {
        return;
    }
    const tr = event.target.closest('tr');
    const id = tr.id.slice(7);
// модальное окно edit
    if (event.target.id === 'buttonMoalEdit') {
        sendRequest(requestUrl + '/' + id).then(userEdit => {
            // заполнение модального окна
            document.getElementById('modalEditId').value = userEdit.id
            document.getElementById('modalEditFirstName').value = userEdit.firstName
            document.getElementById('modalEditLastName').value = userEdit.lastName
            document.getElementById('modalEditAge').value = userEdit.age
            document.getElementById('modalEditEmail').value = userEdit.email
            document.getElementById('modalEditPassword').value = null

            // заполнение ролей
            sendRequest(requestUrl + '/roles').then(roleList => {
                let temp = '';
                if (roleList.length > 0) {
                    roleList.forEach(role => {
                        temp += '<option id="modalEditRole' + role.name + '" ';
                        userEdit.roles.forEach(r => {
                            if (r.name === role.name) {
                                temp += 'selected '
                            }
                        })
                        temp += '>' + role.name + '</option>';
                    })
                }
                document.getElementById('modalEditRoles').innerHTML = temp;
            })
        })
        editModal.show();
    }
    //модальное окно delete
    if (event.target.id === 'buttonMoalDelete') {
        sendRequest(requestUrl + '/' + id).then(userEdit => {
            // заполнение модального окна
            document.getElementById('modalDeleteId').value = userEdit.id
            document.getElementById('modalDeleteFirstName').value = userEdit.firstName
            document.getElementById('modalDeleteLastName').value = userEdit.lastName
            document.getElementById('modalDeleteAge').value = userEdit.age
            document.getElementById('modalDeleteEmail').value = userEdit.email

            // заполнение ролей
            sendRequest(requestUrl + '/roles').then(roleList => {
                let temp = '';
                if (roleList.length > 0) {
                    roleList.forEach(role => {
                        temp += '<option id="modalDeleteRole' + role.name + '" ';
                        userEdit.roles.forEach(r => {
                            if (r.name === role.name) {
                                temp += 'selected '
                            }
                        })
                        temp += '>' + role.name + '</option>';
                    })
                }
                document.getElementById('modalDeleteRoles').innerHTML = temp;
            })
        })
        deleteModal.show();
    }
})

//отправка формы editModal
editeForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const formData = new FormData(editeForm);
    let objectUser = {
        id: formData.get('id'),
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        age: formData.get('age'),
        email: formData.get('email'),
        password: formData.get('password'),
        roles: []
    };
    formData.getAll('roles').forEach(role => {
        objectUser.roles.push({name: role});
    })
    updateUser(objectUser).then(() => {
        getUser(requestUrl + '/' + objectUser.id).then(editedUser => editedUser.json()).then(editedUser => {
            let roleString = "";
            editedUser.roles.forEach(r => {
                roleString += r.name + " ";
            })
            document.getElementById(`userRow${editedUser.id}`).innerHTML =
                "<td>" + editedUser.id + "</td>" +
                "<td>" + editedUser.firstName + "</td>" +
                "<td>" + editedUser.lastName + "</td>" +
                "<td>" + editedUser.age + "</td>" +
                "<td>" + editedUser.email + "</td>" +
                "<td>" + roleString + "</td>" +
                "<td>\n" +
                "<button id =\"buttonMoalEdit\" type=\"button\" class=\"btn btn-info\"\n " +
                "data-target=\"#edit\">\n" +
                " Edit\n" +
                "</button>\n" +
                "</td>\n" +
                "<td>\n" +
                "<button id =\"buttonMoalDelete\" type=\"button\" class=\"btn btn-danger\" " +
                "data-target=\"#delete\">\n" +
                "Delete\n" +
                "</button>\n" +
                "</td>"
        })
    });
    editModal.hide();
});

//отправка формы deleteModal
deleteForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const formData = new FormData(deleteForm);
    let userId = formData.get('id');

    deleteUser(requestUrl + `/${userId}`).then(() => {
        deleteModal.hide();
        fillUsersTable();
    });

});

//add new user
addUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const formData = new FormData(addUserForm);
    let objectNewUser = {
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        age: formData.get('age'),
        email: formData.get('email'),
        password: formData.get('password'),
        roles: []
    };
    formData.getAll('roles').forEach(role => {
        objectNewUser.roles.push({name: role});
    })
    addUser(objectNewUser).then(() => {
        document.getElementById('addFirstName').value = '';
        document.getElementById('addLastName').value = '';
        document.getElementById('addAge').value = '';
        document.getElementById('addEmail').value = '';
        document.getElementById('addPassword').value = '';
        document.getElementById('addRole').value = '';

        fillUsersTable();
        $('#tabs a:first').tab('show');
    });
});


