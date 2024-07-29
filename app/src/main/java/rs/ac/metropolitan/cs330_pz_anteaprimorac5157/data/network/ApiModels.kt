package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val message: String)
data class AuthenticationResponse(val authenticated: Boolean)