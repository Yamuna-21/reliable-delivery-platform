# Jenkins setup (local lab)

This project expects Jenkins to run on a Linux agent that has access to the Docker daemon. Do not expose an unsecured Jenkins instance to the public internet.

## Agent prerequisites

Install and verify:

```bash
java -version       # Java 17+
mvn -version        # Maven 3.9+
docker version      # Docker CLI connected to a daemon
trivy --version     # Trivy scanner
curl --version
```

The Jenkins agent account must be able to run Docker commands. Granting Docker socket access is effectively root-level access on that machine; use a dedicated disposable lab VM/agent, not a personal or production host.

## Create the pipeline job

1. Open Jenkins and choose **New Item**.
2. Enter `reliable-delivery-platform` and select **Pipeline**.
3. Under Pipeline, choose **Pipeline script from SCM**.
4. Select **Git**, enter your repository URL, and set the branch to `*/main`.
5. Set Script Path to `Jenkinsfile`, save, and choose **Build Now**.
6. Open Console Output and inspect each stage.

The `Deploy Locally` stage binds to ports 8080, 9090, and 3000 on the Jenkins agent. Ensure these ports are free. For a shared Jenkins host, change the deployment approach rather than exposing these ports publicly.

## Expected stages

Checkout → Unit Tests → Package → Build Container → Container Security Scan → Deploy Locally (main only) → Smoke Test (main only).

If Trivy reports HIGH/CRITICAL vulnerabilities, inspect the CVE and update the base image/dependencies where appropriate. Do not disable the gate just to make the build green.
