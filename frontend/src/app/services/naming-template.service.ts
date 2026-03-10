import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NamingTemplate, CreateNamingTemplateRequest } from '../models/naming-template.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class NamingTemplateService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/navigation/naming-templates`;

  findAll(): Observable<NamingTemplate[]> {
    return this.http.get<NamingTemplate[]>(this.baseUrl);
  }

  findById(id: number): Observable<NamingTemplate> {
    return this.http.get<NamingTemplate>(`${this.baseUrl}/${id}`);
  }

  create(request: CreateNamingTemplateRequest): Observable<NamingTemplate> {
    return this.http.post<NamingTemplate>(this.baseUrl, request);
  }

  update(id: number, request: CreateNamingTemplateRequest): Observable<NamingTemplate> {
    return this.http.put<NamingTemplate>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
