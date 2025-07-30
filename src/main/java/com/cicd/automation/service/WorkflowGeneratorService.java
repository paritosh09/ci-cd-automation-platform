package com.cicd.automation.service;

import com.cicd.automation.model.Pipeline;
import org.springframework.stereotype.Service;

@Service
public class WorkflowGeneratorService {

    public String generateWorkflow(Pipeline pipeline) {
        return String.format(
                "name: %s CI/CD Pipeline\n" +
                        "\n" +
                        "on:\n" +
                        "  push:\n" +
                        "    branches: [ %s ]\n" +
                        "  pull_request:\n" +
                        "    branches: [ %s ]\n" +
                        "  workflow_dispatch:\n" +
                        "\n" +
                        "jobs:\n" +
                        "  test:\n" +
                        "    runs-on: ubuntu-latest\n" +
                        "\n" +
                        "    steps:\n" +
                        "    - uses: actions/checkout@v3\n" +
                        "\n" +
                        "    - name: Set up JDK 17\n" +
                        "      uses: actions/setup-java@v3\n" +
                        "      with:\n" +
                        "        java-version: '17'\n" +
                        "        distribution: 'temurin'\n" +
                        "\n" +
                        "    - name: Cache Maven dependencies\n" +
                        "      uses: actions/cache@v3\n" +
                        "      with:\n" +
                        "        path: ~/.m2\n" +
                        "        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}\n" +
                        "        restore-keys: ${{ runner.os }}-m2\n" +
                        "\n" +
                        "    - name: Run tests\n" +
                        "      run: mvn clean test\n" +
                        "\n" +
                        "    - name: Generate test report\n" +
                        "      uses: dorny/test-reporter@v1\n" +
                        "      if: success() || failure()\n" +
                        "      with:\n" +
                        "        name: Maven Tests\n" +
                        "        path: target/surefire-reports/*.xml\n" +
                        "        reporter: java-junit\n" +
                        "\n" +
                        "  build:\n" +
                        "    runs-on: ubuntu-latest\n" +
                        "    needs: test\n" +
                        "    if: github.ref == 'refs/heads/%s'\n" +
                        "\n" +
                        "    steps:\n" +
                        "    - uses: actions/checkout@v3\n" +
                        "\n" +
                        "    - name: Set up JDK 17\n" +
                        "      uses: actions/setup-java@v3\n" +
                        "      with:\n" +
                        "        java-version: '17'\n" +
                        "        distribution: 'temurin'\n" +
                        "\n" +
                        "    - name: Build application\n" +
                        "      run: mvn clean compile\n" +
                        "\n" +
                        "    - name: Build Docker image\n" +
                        "      run: |\n" +
                        "        docker build -t %s:${{ github.sha }} .\n" +
                        "        docker build -t %s:latest .\n" +
                        "\n" +
                        "    - name: Run security scan\n" +
                        "      uses: securecodewarrior/github-action-add-sarif@v1\n" +
                        "      with:\n" +
                        "        sarif-file: 'security-scan-results.sarif'\n" +
                        "\n" +
                        "  deploy:\n" +
                        "    runs-on: ubuntu-latest\n" +
                        "    needs: build\n" +
                        "    if: github.ref == 'refs/heads/%s'\n" +
                        "    environment: production\n" +
                        "\n" +
                        "    steps:\n" +
                        "    - name: Deploy to staging\n" +
                        "      run: |\n" +
                        "        echo \"Deploying %s to staging environment\"\n" +
                        "        # Add your deployment commands here\n" +
                        "\n" +
                        "    - name: Run integration tests\n" +
                        "      run: |\n" +
                        "        echo \"Running integration tests\"\n" +
                        "        # Add integration test commands here\n" +
                        "\n" +
                        "    - name: Deploy to production\n" +
                        "      run: |\n" +
                        "        echo \"Deploying %s to production environment\"\n" +
                        "        # Add production deployment commands here\n",
                pipeline.getName(),
                pipeline.getBranch(),
                pipeline.getBranch(),
                pipeline.getBranch(),
                pipeline.getRepositoryName(),
                pipeline.getRepositoryName(),
                pipeline.getBranch(),
                pipeline.getName(),
                pipeline.getName());
    }

    public String generateSpringBootWorkflow(Pipeline pipeline) {
        return generateWorkflow(pipeline); // Uses the same template for now
    }

    public String generateDockerWorkflow(Pipeline pipeline) {
        return String.format(
                "name: %s Docker Build\n" +
                        "\n" +
                        "on:\n" +
                        "  push:\n" +
                        "    branches: [ %s ]\n" +
                        "  workflow_dispatch:\n" +
                        "\n" +
                        "jobs:\n" +
                        "  docker-build:\n" +
                        "    runs-on: ubuntu-latest\n" +
                        "\n" +
                        "    steps:\n" +
                        "    - uses: actions/checkout@v3\n" +
                        "\n" +
                        "    - name: Set up Docker Buildx\n" +
                        "      uses: docker/setup-buildx-action@v2\n" +
                        "\n" +
                        "    - name: Login to DockerHub\n" +
                        "      uses: docker/login-action@v2\n" +
                        "      with:\n" +
                        "        username: ${{ secrets.DOCKERHUB_USERNAME }}\n" +
                        "        password: ${{ secrets.DOCKERHUB_TOKEN }}\n" +
                        "\n" +
                        "    - name: Build and push Docker image\n" +
                        "      uses: docker/build-push-action@v4\n" +
                        "      with:\n" +
                        "        context: .\n" +
                        "        push: true\n" +
                        "        tags: |\n" +
                        "          %s:latest\n" +
                        "          %s:${{ github.sha }}\n" +
                        "        cache-from: type=gha\n" +
                        "        cache-to: type=gha,mode=max\n",
                pipeline.getName(),
                pipeline.getBranch(),
                pipeline.getRepositoryName(),
                pipeline.getRepositoryName());
    }
}
