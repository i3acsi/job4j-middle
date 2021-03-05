package ru.job4j.hibernate.hql.store;

import lombok.AllArgsConstructor;
import ru.job4j.hibernate.hql.entity.Candidate;
import ru.job4j.hibernate.hql.entity.Vacancy;
import ru.job4j.hibernate.hql.entity.VacancyBase;

import java.util.List;

@AllArgsConstructor
public class CandidateRepo {
    private final Store store;


    public void create(List<Candidate> candidates) {
        store.tx(session -> {
            candidates.forEach(c -> {
                c.getVacancyBase().getVacancies().forEach(session::persist);
            });
            candidates.forEach(c -> {
                session.persist(c.getVacancyBase());
            });
            candidates.forEach(session::persist);
            return true;
        });
    }

    @SuppressWarnings("unchecked")
    public List<Candidate> findAllCandidates() {
        return store.tx(session ->
                session.createQuery("select distinct c from Candidate c join fetch c.vacancyBase vb join fetch vb.vacancies").list()
        );
    }

    public Candidate findCandidateById(Long id) {
        return store.tx(session ->
                (Candidate) session.createQuery(
                        "select distinct c from Candidate c " +
                                "join fetch c.vacancyBase vb " +
                                "join fetch vb.vacancies where c.id = :fId")
                        .setParameter("fId", id)
                        .uniqueResult()
        );
    }

    public Candidate findCandidateByName(String name) {
        return store.tx(session ->
                (Candidate) session.createQuery(
                        "select distinct c from Candidate c " +
                                "join fetch c.vacancyBase vb " +
                                "join fetch vb.vacancies where c.name = :fName")
                        .setParameter("fName", name)
                        .uniqueResult()
        );
    }

    public Candidate updateCandidate(Candidate candidate) {
        return store.tx(session -> {
            candidate.getVacancyBase().getVacancies().forEach(session::update);
            session.update(candidate.getVacancyBase());
            session.update(candidate);
            return candidate;
        });
    }

    public void deleteCandidate(Long id) {
        store.delete(id, Candidate.class);
    }

    public static void main(String[] args) {
        Store store = new Store();
        CandidateRepo candidateRepo = new CandidateRepo(store);

        Vacancy vacancy1_1 = Vacancy.of("vacancy1_1", "desc1_1");
        Vacancy vacancy1_2 = Vacancy.of("vacancy1_2", "desc1_2");
        Vacancy vacancy2_1 = Vacancy.of("vacancy2_1", "desc2_1");
        Vacancy vacancy2_2 = Vacancy.of("vacancy2_2", "desc2_2");
        Vacancy vacancy3_1 = Vacancy.of("vacancy3_1", "desc3_1");
        Vacancy vacancy3_2 = Vacancy.of("vacancy3_2", "desc3_2");

        VacancyBase vacancyBase1 = VacancyBase.of("VB1");
        VacancyBase vacancyBase2 = VacancyBase.of("VB2");
        VacancyBase vacancyBase3 = VacancyBase.of("VB3");

        vacancy1_1.setVacancyBase(vacancyBase1);
        vacancy1_2.setVacancyBase(vacancyBase1);

        vacancy2_1.setVacancyBase(vacancyBase2);
        vacancy2_2.setVacancyBase(vacancyBase2);

        vacancy3_1.setVacancyBase(vacancyBase3);
        vacancy3_2.setVacancyBase(vacancyBase3);

        vacancyBase1.getVacancies().addAll(List.of(vacancy1_1, vacancy1_2));
        vacancyBase2.getVacancies().addAll(List.of(vacancy2_1, vacancy2_2));
        vacancyBase3.getVacancies().addAll(List.of(vacancy3_1, vacancy3_2));

        Candidate candidate1 = Candidate.of("candidate1", 100L, 100500L, vacancyBase1);
        Candidate candidate2 = Candidate.of("candidate2", 200L, 200500L, vacancyBase2);
        Candidate candidate3 = Candidate.of("candidate3", 300L, 300500L, vacancyBase3);

        List<Candidate> candidates = List.of(candidate1, candidate2, candidate3);
        candidateRepo.create(candidates);

        List<Candidate> list = candidateRepo.findAllCandidates();
        System.out.println("FIND ALL: ");
        list.forEach(System.out::println);
        System.out.println("##############\r\n");

        Long id0 = list.get(0).getId();
        System.out.println("FIND BY ID " + id0 + " : ");
        System.out.println(candidateRepo.findCandidateById(id0));
        System.out.println("##############\r\n");

        String name = list.get(1).getName();
        System.out.println("FIND BY NAME \"" + name + "\" : ");
        System.out.println(candidateRepo.findCandidateByName(name));
        System.out.println("##############\r\n");

        Long id2 = list.get(2).getId();
        Candidate c = candidateRepo.findCandidateById(id2);
        System.out.println("UPDATE, SET EXP = 0, VACANCY_NAME = newName : " + c);
        c.setExperience(0L);
        c.getVacancyBase().getVacancies().get(0).setName("newName");
        System.out.println("UPDATED: " + candidateRepo.updateCandidate(c));
        System.out.println("FIND BY ID " + candidateRepo.findCandidateById(id2));
        System.out.println("##############\r\n");

        candidateRepo.deleteCandidate(id2);
        list = candidateRepo.findAllCandidates();
        System.out.println("DELETED WITH ID " + id2 + " : " + candidateRepo.findCandidateById(id2));
        System.out.println("FIND ALL: ");
        list.forEach(System.out::println);
    }
}
