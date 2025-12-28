export interface ApiError extends Error{
    status?: number;
    data?: unknown;
}

export async function apiFetch<T>(
    path: string,
    options: RequestInit = {}
): Promise<T>{
    const res = await fetch(path, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {}),
        },
        ...options,
    });

    const text = await res.text();
    const data = text ? JSON.parse(text) : null;

    if (!res.ok) {
        const err: ApiError = new Error(
            (data && data.message) || res.statusText
        );
        err.status = res.status;
        err.data = data;
        throw err;
    }

    return data as T;
}