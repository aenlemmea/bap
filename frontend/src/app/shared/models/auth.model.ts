export enum Role {
  ROLE_GUEST = 'guest',
  ROLE_HOST = 'host',
}

export interface UserProfile {
  email: string;
  fullName: string;
  role: Role;
}

export interface LoginRequest {
  email: string,
  password: string,
}

export interface RegisterUserRequest {
  email: string,
  password: string,
  fullName: string,
  role: Role
}

export interface AuthorizeResponse {
  email: string;
  fullName: string;
  role: Role;
}
