import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TCode, CreateTCodeRequest } from '../models/tcode.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class TCodeService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/navigation/tcodes`;

  findAll(): Observable<TCode[]> {
    return this.http.get<TCode[]>(this.baseUrl);
  }

  findById(id: number): Observable<TCode> {
    return this.http.get<TCode>(`${this.baseUrl}/${id}`);
  }

  findByCode(code: string): Observable<TCode> {
    return this.http.get<TCode>(`${this.baseUrl}/code/${code}`);
  }

  findByModule(module: string): Observable<TCode[]> {
    return this.http.get<TCode[]>(`${this.baseUrl}/module/${module}`);
  }

  findAllActive(): Observable<TCode[]> {
    return this.http.get<TCode[]>(`${this.baseUrl}/active`);
  }

  create(request: CreateTCodeRequest): Observable<TCode> {
    return this.http.post<TCode>(this.baseUrl, request);
  }

  update(id: number, request: CreateTCodeRequest): Observable<TCode> {
    return this.http.put<TCode>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
