import { Injectable, Inject } from '@angular/core';
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx'
import { Headers, RequestMethod, RequestOptions } from '@angular/http';

interface SearchRequest {
    pageIndex?: number;
    pageSize?: number;
}

export class RestService {
  constructor(private url: string, private http: Http) {
  }

  private getHeaders(): Headers {
    const headers = new Headers();
    headers.append('Accept', 'application/json');
    return headers;
  }

  search(searchRequest?: SearchRequest): Observable<Response> {
    const options = new RequestOptions({headers: this.getHeaders()});
    return this.http.get(`${this.url}`, options);
  }

  get(id): Observable<Response> {
    const options = new RequestOptions({headers: this.getHeaders()});
    return this.http.get('${this.url}/${id}', options);
  }

  save(id, item): Observable<Response> {
    const options = new RequestOptions({headers: this.getHeaders()});
    return this.http.put('${this.url}/${id}', JSON.stringify(item), options);
  }

  update(id, item): Observable<Response> {
    const options = new RequestOptions({headers: this.getHeaders()});
    return this.http.post('${this.url}/${id}', JSON.stringify(item), options);
  }
}

