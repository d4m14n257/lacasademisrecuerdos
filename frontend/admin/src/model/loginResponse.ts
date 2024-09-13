type User = { 
    id: string,
    username: string,
    email: string, 
    first_name: string,
    last_name: string
}

export type LoginResponse = {
    user : User,
    token: string,
    tokenType : string
}