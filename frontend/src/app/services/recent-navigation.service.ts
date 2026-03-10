import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecentNavigation, CreateRecentNavigationRequest } from '../models/recent-navigation.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RecentNavigationService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/navigation/recent`;

  findById(id: number): Observable<RecentNavigation> {
    return this.http.get<RecentNavigation>(`${this.baseUrl}/${id}`);
  }

  findByUserId(userId: number): Observable<RecentNavigation[]> {
    return this.http.get<RecentNavigation[]>(`${this.baseUrl}/user/${userId}`);
  }

  record(request: CreateRecentNavigationRequest): Observable<RecentNavigation> {
    return this.http.post<RecentNavigation>(this.baseUrl, request);
  }

  deleteByUserId(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/user/${userId}`);
  }
}
