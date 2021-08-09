import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Block } from './block.model';

@Injectable({
  providedIn: 'root'
})
export class BlockService {

  baseUrl = 'http://localhost:8080/block';
  constructor(protected http: HttpClient) { }

  getBlock(): Observable<Block> {
    return this.http.get(this.baseUrl) as Observable<Block>;
  }
}
