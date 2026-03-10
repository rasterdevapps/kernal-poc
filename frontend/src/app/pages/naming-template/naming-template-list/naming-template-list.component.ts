import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NamingTemplateService } from '../../../services/naming-template.service';
import { NamingTemplate, CreateNamingTemplateRequest } from '../../../models/naming-template.model';

@Component({
  selector: 'app-naming-template-list',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './naming-template-list.component.html',
  styleUrl: './naming-template-list.component.scss'
})
export class NamingTemplateListComponent implements OnInit {
  private readonly namingTemplateService = inject(NamingTemplateService);
  private readonly fb = inject(FormBuilder);

  readonly templates = signal<NamingTemplate[]>([]);
  readonly loading = signal(true);
  readonly showForm = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly form: FormGroup = this.fb.group({
    entityType: ['', [Validators.required, Validators.maxLength(100)]],
    pattern: ['', [Validators.required, Validators.maxLength(255)]],
    description: ['', Validators.maxLength(255)],
    example: ['', Validators.maxLength(255)]
  });

  ngOnInit(): void {
    this.loadTemplates();
  }

  loadTemplates(): void {
    this.loading.set(true);
    this.namingTemplateService.findAll().subscribe({
      next: (templates) => {
        this.templates.set(templates);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load naming templates from API:', err.message, err);
        this.loading.set(false);
      }
    });
  }

  openCreateForm(): void {
    this.editingId.set(null);
    this.form.reset();
    this.showForm.set(true);
  }

  openEditForm(template: NamingTemplate): void {
    this.editingId.set(template.id);
    this.form.patchValue({
      entityType: template.entityType,
      pattern: template.pattern,
      description: template.description,
      example: template.example
    });
    this.showForm.set(true);
  }

  cancelForm(): void {
    this.showForm.set(false);
    this.form.reset();
    this.editingId.set(null);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }

    const request: CreateNamingTemplateRequest = this.form.value;
    const id = this.editingId();

    if (id !== null) {
      this.namingTemplateService.update(id, request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadTemplates();
        }
      });
    } else {
      this.namingTemplateService.create(request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadTemplates();
        }
      });
    }
  }

  deleteTemplate(id: number): void {
    if (!confirm('Are you sure you want to delete this naming template?')) {
      return;
    }
    this.namingTemplateService.delete(id).subscribe({
      next: () => this.loadTemplates()
    });
  }
}
