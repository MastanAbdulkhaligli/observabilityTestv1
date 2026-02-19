package sosial.observabilitytest.service;

import org.springframework.stereotype.Service;
import sosial.observabilitytest.repository.TestRepository;

@Service
public class TestService {

    private final TestRepository repo;

    public TestService(TestRepository repo) {
        this.repo = repo;
    }

    public String ok() {
        repo.touch();
        return "ok";
    }

    public String fail() {
        repo.touch();
        throw new RuntimeException("boom");
    }
}
