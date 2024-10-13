import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService
{
    private readonly TOKEN_KEY = 'token';


    /**
     * Set the value for a given key in the LocalStorage
     *
     * @param key string
     * @param value any
     * @returns void
     */
    public setItem(key: string, value: any): void {
        localStorage.setItem(key, JSON.stringify(value));
    }

    /**
     * Get the value for a given key in the LocalStorage
     *
     * @param key string
     * @returns any
     */
    public getItem(key: string) : any {
        const value = localStorage.getItem(key);

        return (value)
            ? JSON.parse(value)
            : null;
    }

    /**
     * Remove the value for a a given key in the LocalStorage
     *
     * @param key string
     * @returns void
     */
    public removeItem(key: string): void {
        localStorage.removeItem(key);
    }

    public getBearerToken(): string {
        return this.getItem(this.TOKEN_KEY);
    }

    public setBearerToken(token: string): void {
        const bearerToken = `Bearer ${token}`;        
        this.setItem(this.TOKEN_KEY, bearerToken);
    }

    public removeBearerToken(): void {
        this.removeItem(this.TOKEN_KEY);
    }

}