# Contributing to SmartGrid Manager

First off, thank you for considering contributing to SmartGrid Manager! It's people like you that make SmartGrid Manager such a great tool.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Reporting Bugs](#reporting-bugs)
- [Suggesting Enhancements](#suggesting-enhancements)

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainers.

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/smartgrid-manager.git
   cd smartgrid-manager
   ```
3. **Set up the development environment** following [SETUP.md](SETUP.md)
4. **Create a branch** for your changes:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## How to Contribute

### Types of Contributions

We welcome many different types of contributions:

- 🐛 **Bug fixes**
- ✨ **New features**
- 📝 **Documentation improvements**
- 🎨 **UI/UX enhancements**
- ⚡ **Performance improvements**
- ✅ **Test coverage**
- 🔒 **Security fixes**

### Areas That Need Help

- Adding unit and integration tests
- Improving error handling
- Adding user authentication and authorization
- Mobile-responsive UI improvements
- Additional device type support
- Export functionality (CSV, Excel, PDF)
- Email notification system
- Advanced analytics and reporting

## Development Workflow

### 1. Set Up Your Development Environment

Follow the complete setup guide in [SETUP.md](SETUP.md):
- Install Java 11, Maven, MySQL, Tomcat
- Configure database
- Build and deploy the application

### 2. Find or Create an Issue

- Check existing issues for something to work on
- If you want to add a new feature, create an issue first to discuss it
- Comment on the issue to let others know you're working on it

### 3. Make Your Changes

- Write clean, maintainable code
- Follow the coding standards (see below)
- Add tests for new functionality
- Update documentation as needed
- Test your changes thoroughly

### 4. Test Your Changes

```bash
# Build the project
mvn clean package

# Run tests
mvn test

# Deploy and test manually
# Access: http://localhost:8080/smartgridmanager/
```

### 5. Commit Your Changes

Follow our commit message guidelines (see below):

```bash
git add .
git commit -m "feat: add device export functionality"
```

### 6. Push and Create Pull Request

```bash
git push origin feature/your-feature-name
```

Then create a Pull Request on GitHub.

## Coding Standards

### Java Code Style

Follow standard Java conventions:

```java
// Good: Clear, descriptive names
public class DeviceManager {
    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    public Device findDeviceById(Long deviceId) {
        // Implementation
    }
}

// Bad: Unclear names, poor formatting
public class dm {
    private int x=3;
    public Device find(Long id){return null;}
}
```

**Key Points**:
- Use meaningful variable and method names
- Follow camelCase for methods and variables
- Follow PascalCase for class names
- Use UPPER_SNAKE_CASE for constants
- Add JavaDoc comments for public methods
- Keep methods focused and concise (< 30 lines ideally)
- Use proper indentation (4 spaces)

### Documentation Standards

- Add JavaDoc comments for all public classes and methods
- Update relevant documentation files (README, API, etc.)
- Include code examples where helpful
- Keep documentation up-to-date with code changes

Example JavaDoc:

```java
/**
 * Retrieves a device by its unique identifier.
 *
 * @param deviceId the unique identifier of the device
 * @return the Device object if found, null otherwise
 * @throws IllegalArgumentException if deviceId is null
 */
public Device findDeviceById(Long deviceId) {
    // Implementation
}
```

### SQL and Database

- Use parameterized queries (JPQL) to prevent SQL injection
- Add appropriate indexes for new queries
- Test performance with large datasets
- Document any schema changes

### JSP and Frontend

- Use semantic HTML
- Follow Bootstrap conventions
- Keep JavaScript minimal and organized
- Ensure responsive design
- Test in multiple browsers

## Commit Guidelines

We follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation only changes
- `style`: Code style changes (formatting, missing semicolons, etc.)
- `refactor`: Code refactoring without changing functionality
- `perf`: Performance improvements
- `test`: Adding or updating tests
- `chore`: Build process or auxiliary tool changes

### Examples

```bash
# Feature
git commit -m "feat(api): add endpoint for device statistics"

# Bug fix
git commit -m "fix(dao): resolve N+1 query issue in StatsDao"

# Documentation
git commit -m "docs(readme): add installation troubleshooting section"

# Refactor
git commit -m "refactor(servlet): extract common logic to utility class"

# With body and footer
git commit -m "feat(alerts): add email notification for critical alerts

- Implement email service with JavaMail
- Add configuration for SMTP settings
- Create email templates for different alert types

Closes #42"
```

## Pull Request Process

### Before Submitting

1. ✅ Update documentation if needed
2. ✅ Add or update tests
3. ✅ Ensure all tests pass
4. ✅ Run the application and test manually
5. ✅ Update CHANGELOG.md if applicable
6. ✅ Rebase on the latest main branch

### Pull Request Template

When creating a PR, include:

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Manual testing completed
- [ ] All tests passing

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
- [ ] No new warnings generated

## Screenshots (if applicable)
Add screenshots for UI changes

## Related Issues
Closes #issue_number
```

### Review Process

1. A maintainer will review your PR
2. Address any requested changes
3. Once approved, your PR will be merged
4. Your contribution will be credited in the release notes

## Reporting Bugs

### Before Submitting a Bug Report

- Check existing issues to avoid duplicates
- Verify the bug exists in the latest version
- Collect relevant information (logs, screenshots, etc.)

### Bug Report Template

```markdown
## Bug Description
A clear description of the bug

## Steps to Reproduce
1. Go to '...'
2. Click on '...'
3. Scroll down to '...'
4. See error

## Expected Behavior
What you expected to happen

## Actual Behavior
What actually happened

## Environment
- OS: [e.g., Ubuntu 20.04]
- Java Version: [e.g., 11.0.12]
- Tomcat Version: [e.g., 10.1.5]
- MySQL Version: [e.g., 8.0.30]
- Browser (if applicable): [e.g., Chrome 95]

## Logs
Paste relevant logs here

## Screenshots
Add screenshots if applicable
```

## Suggesting Enhancements

### Before Submitting

- Check if the enhancement has already been suggested
- Consider if it aligns with the project goals
- Think about how it would benefit other users

### Enhancement Request Template

```markdown
## Enhancement Description
A clear description of the enhancement

## Problem It Solves
What problem does this enhancement address?

## Proposed Solution
How would you implement this?

## Alternatives Considered
What other solutions did you consider?

## Additional Context
Any other context, mockups, or examples
```

## Development Tips

### Database Development

```bash
# Reset database for testing
mysql -u smartgrid_user -p smartgrid

DROP DATABASE smartgrid;
CREATE DATABASE smartgrid CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Debugging

- Use your IDE's debugger
- Enable SQL logging in persistence.xml:
  ```xml
  <property name="hibernate.show_sql" value="true"/>
  <property name="hibernate.format_sql" value="true"/>
  ```
- Check Tomcat logs: `tail -f $CATALINA_HOME/logs/catalina.out`

### Performance Testing

```java
// Example: Test data generation performance
long start = System.currentTimeMillis();
DataGenerator.generateRandomData(100, 1000);
long duration = System.currentTimeMillis() - start;
System.out.println("Generation took: " + duration + "ms");
```

## Questions?

If you have questions about contributing:

1. Check existing documentation (README, SETUP, API, ARCHITECTURE)
2. Search existing issues and discussions
3. Open a new issue with the "question" label
4. Reach out to the maintainers

## Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes
- GitHub contributors page

Thank you for contributing to SmartGrid Manager! 🎉

---

**Happy Coding!** 💻
