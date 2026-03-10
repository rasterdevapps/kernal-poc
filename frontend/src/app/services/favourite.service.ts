import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Favourite, CreateFavouriteRequest } from '../models/favourite.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class FavouriteService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/navigation/favourites`;

  findById(id: number): Observable<Favourite> {
    return this.http.get<Favourite>(`${this.baseUrl}/${id}`);
  }

  findByUserId(userId: number): Observable<Favourite[]> {
    return this.http.get<Favourite[]>(`${this.baseUrl}/user/${userId}`);
  }

  create(request: CreateFavouriteRequest): Observable<Favourite> {
    return this.http.post<Favourite>(this.baseUrl, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
