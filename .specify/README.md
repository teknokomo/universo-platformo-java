# .specify Directory - Spec Kit Documentation Structure

This directory contains all Spec Kit documentation and tools for the Universo Platformo Java project, organized according to [Spec Kit standards](https://spec-kit.com).

## Directory Structure

```
.specify/
├── memory/          # Project memory and historical documentation
├── specs/           # Feature specifications (one directory per feature)
├── templates/       # Templates for specs, plans, and tasks
└── scripts/         # Automation scripts for Spec Kit workflows
```

## Directories

### 📝 memory/
Contains the project constitution and historical documentation:
- **constitution.md**: Project principles and guidelines (NON-NEGOTIABLE)
- Architecture analyses and audit reports
- Best practices documentation
- Research summaries
- Implementation roadmaps

### 📋 specs/
Feature specifications, organized by feature number:
- Each feature has its own directory: `NNN-feature-name/`
- Each feature directory contains:
  - `spec.md` - Feature specification
  - `plan.md` - Implementation plan
  - `tasks.md` - Task breakdown
  - `data-model.md` - Data model (optional)
  - `research.md` - Research notes (optional)
  - `quickstart.md` - Quick start guide (optional)
  - `contracts/` - API contracts (optional)
  - `checklists/` - Validation checklists (optional)

### 📄 templates/
Standard templates for creating new specifications:
- `spec-template.md` - Feature specification template
- `plan-template.md` - Implementation plan template
- `tasks-template.md` - Task breakdown template
- `checklist-template.md` - Checklist template
- `agent-file-template.md` - Agent configuration template
- `package-readme-template.md` - Package README template (English)
- `package-readme-template-ru.md` - Package README template (Russian)

### 🔧 scripts/
Automation scripts for Spec Kit workflows:
- `bash/check-prerequisites.sh` - Validate prerequisites for workflows
- `bash/common.sh` - Common functions and path resolution
- `bash/create-new-feature.sh` - Create new feature specification
- `bash/setup-plan.sh` - Setup implementation plan
- `bash/update-agent-context.sh` - Update agent context

## Usage

### For Spec Kit Agents

All Spec Kit agents (specified in `.github/agents/`) automatically access this directory:
- `/speckit.specify` - Create new feature specifications
- `/speckit.clarify` - Clarify ambiguities in specifications
- `/speckit.plan` - Generate implementation plans
- `/speckit.tasks` - Break down plans into tasks
- `/speckit.analyze` - Analyze consistency across artifacts
- `/speckit.implement` - Implement features
- `/speckit.checklist` - Generate validation checklists

### For Developers

To work with feature specifications:

1. **Create a new feature branch** (named `NNN-feature-name`):
   ```bash
   ./.specify/scripts/bash/create-new-feature.sh "Your feature description"
   ```

2. **Check prerequisites** for current feature:
   ```bash
   ./.specify/scripts/bash/check-prerequisites.sh --json
   ```

3. **Navigate to feature directory**:
   ```bash
   cd .specify/specs/NNN-feature-name/
   ```

## Integration with GitHub Agents

The `.github/agents/` directory contains agent configurations that reference this structure:
- **tasks** mode: Accesses `.specify/specs/` to generate implementation tasks
- **analytics** mode: Accesses `.specify/memory/constitution.md` and specs for analysis

All paths in scripts and agent configurations now correctly reference `.specify/specs/` instead of the legacy `/specs/` location.

## Migration Notes

As of 2025-11-24, the documentation structure has been reorganized:
- ✅ Moved all feature specs from `/specs/` to `.specify/specs/`
- ✅ Constitution and memory documents already in `.specify/memory/`
- ✅ Templates already in `.specify/templates/`
- ✅ Scripts updated to reference new paths
- ✅ Agent configurations updated to access `.specify/`

This ensures all project documentation is centralized and accessible to all agents.
