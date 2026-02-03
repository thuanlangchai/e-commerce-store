// Utility to decode JWT token
export interface JwtPayload {
  sub: string;
  type: string;
  id: number;
  email: string;
  phone?: string;
  role: string;
  iat?: number;
  exp?: number;
}

export const decodeJwt = (token: string): JwtPayload | null => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      window
        .atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Error decoding JWT:', error);
    return null;
  }
};

export const getRoleFromToken = (token: string): string | null => {
  const payload = decodeJwt(token);
  return payload?.role || null;
};

export const getUserFromToken = (token: string): { id: number; username: string; email: string; phone?: string; role: string } | null => {
  const payload = decodeJwt(token);
  if (!payload) return null;
  
  return {
    id: payload.id,
    username: payload.sub,
    email: payload.email,
    phone: payload.phone,
    role: payload.role,
  };
};
