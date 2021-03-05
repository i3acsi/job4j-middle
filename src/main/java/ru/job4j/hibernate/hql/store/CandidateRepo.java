package ru.job4j.hibernate.hql.store;

import ru.job4j.hibernate.hql.entity.Candidate;

import java.util.List;

public class CandidateRepo {
    private final Store store;

    public CandidateRepo() {
        this.store = new Store();
    }

    public void create(List<Candidate> candidates) {
        store.create(candidates);
    }

    public List<Candidate> findAllCandidates() {
        return store.findAll(Candidate.class);
    }

    public Candidate findCandidateById(Long id) {
        return store.findById(id, Candidate.class);
    }

    public Candidate findCandidateByName(String name) {
        return store.findBy(name, "name", Candidate.class);
    }

    public Candidate updateCandidate(Candidate candidate) {
        return store.update(candidate);
    }

    public void deleteCandidate(Long id) {
        store.delete(id, Candidate.class);
    }

    public static void main(String[] args) {
        CandidateRepo repo = new CandidateRepo();
        List<Candidate> candidates = List.of(
                Candidate.of("candidate1", 150L, 100000L),
                Candidate.of("candidate2", 250L, 200000L),
                Candidate.of("candidate3", 350L, 300000L)
        );

        try {
            repo.create(candidates);
        } catch (Exception e) {
            System.out.println("not created");
        }
        List<Candidate> list = repo.findAllCandidates();
        System.out.println("FIND ALL: ");
        list.forEach(System.out::println);
        System.out.println("##############\r\n");

        Long id0 = list.get(0).getId();
        System.out.println("FIND BY ID " + id0 + " : ");
        System.out.println(repo.findCandidateById(id0));
        System.out.println("##############\r\n");

        String name = list.get(1).getName();
        System.out.println("FIND BY NAME \"" + name + "\" : ");
        System.out.println(repo.findCandidateByName(name));
        System.out.println("##############\r\n");

        Long id2 = list.get(2).getId();
        Candidate c = repo.findCandidateById(id2);
        System.out.println("UPDATE, SET EXP = 0: " + c);
        c.setExperience(0L);
        System.out.println("UPDATED: " + repo.updateCandidate(c));
        System.out.println("FIND BY ID " + repo.findCandidateById(id2));
        System.out.println("##############\r\n");

        repo.deleteCandidate(id2);
        list = repo.findAllCandidates();
        System.out.println("DELETED WITH ID " + id2 + " : " + repo.findCandidateById(id2));
        System.out.println("FIND ALL: ");
        list.forEach(candidate -> {
            System.out.println(candidate);
            repo.deleteCandidate(candidate.getId());
        });
        System.out.println("DELETE ALL");

    }
}
