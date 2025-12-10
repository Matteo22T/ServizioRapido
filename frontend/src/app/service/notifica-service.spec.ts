import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing'; // <--- Importante
import { NotificaService } from './notifica-service';

describe('NotificaService', () => {
  let service: NotificaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // <--- Simuliamo il modulo HTTP
      providers: [NotificaService]
    });
    service = TestBed.inject(NotificaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
