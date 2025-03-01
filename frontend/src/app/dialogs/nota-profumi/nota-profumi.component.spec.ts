import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotaProfumiComponent } from './nota-profumi.component';

describe('NotaProfumiComponent', () => {
  let component: NotaProfumiComponent;
  let fixture: ComponentFixture<NotaProfumiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotaProfumiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotaProfumiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
