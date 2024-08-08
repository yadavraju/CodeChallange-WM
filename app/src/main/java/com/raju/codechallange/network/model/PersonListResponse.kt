package com.raju.codechallange.network.model

/*
* {
"people": [
{
"name": "Russ",
"language": "swift"
},
{
"name": "Wes",
"language": "dart"
},
{
"name": "Tania",
"language": "kotlin"
},
{
"name": "Robert",
"language": "kotlin"
},
{
"name": "Leilah",
"language": "swift"
},
{
"name": "Bastien",
"language": "swift"
},
{
"name": "Alan",
"language": "swift"
},
{
"name": "Lee",
"language": "swift"
},
{
"name": "Margie"
}
]
}
* */
data class PersonListResponse(val people: List<Person>)

data class Person(val name: String, val language: String?)