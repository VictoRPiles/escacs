let loggedUser: User;

class User {
    readonly id: number;
    readonly username: string;
    readonly email: string;

    constructor(id: number, username: string, email: string) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public toString = (): string => {
        return `User (id: ${this.id}, username: ${this.username}, email: ${this.email})`;
    };
}