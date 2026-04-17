
const BASE_URL: string = import.meta.env.VITE_API_URL || '';

export const api = {

  async post<T>(endpoint: string, body: Record<string, unknown> = {}): Promise<T> {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    });

    return handleResponse<T>(response);
  },

  async get<T>(endpoint:string): Promise<T> {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    return handleResponse<T>(response);
  }
};

// Centralized error handling and JSON parsing
async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to fetch response from backend');
  }
  return response.json() as Promise<T>;
}