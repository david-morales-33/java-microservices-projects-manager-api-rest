package com.microservice.administrative.project.domain;

import com.microservice.administrative.card.domain.Card;
import com.microservice.administrative.card.domain.CardDTO;
import com.microservice.administrative.funcionality.domain.Funcionality;
import com.microservice.administrative.module.domain.ModuleDTO;
import com.microservice.administrative.module.domain.ModuleNotFoundException;
import com.microservice.administrative.shared.domain.ModuleId;
import com.microservice.administrative.shared.domain.ProjectId;
import com.microservice.administrative.team.domain.Team;
import com.microservice.administrative.team.domain.TeamDTO;
import com.microservice.administrative.module.domain.Module;

import java.util.HashMap;
import java.util.List;

public final class ProjectModulesContainer extends Project {
    private final HashMap<Integer, Module> moduleList;
    private final ProjectState state;
    private ProjectModulesCounter modulesCounter;

    public ProjectModulesContainer(
            ProjectId id,
            ProjectName name,
            ProjectFuncionalitiesCounter funcionalitiesCounter,
            HashMap<String, Team> teamList,
            HashMap<String, Card> cardList,
            HashMap<Integer, Module> moduleList
    ) {
        super(id, name, funcionalitiesCounter, teamList, cardList);
        this.moduleList = moduleList;
        this.modulesCounter = new ProjectModulesCounter(moduleList.size());
        this.state = new ProjectState(0);
    }

    public static ProjectModulesContainer create(
            ProjectId id,
            ProjectName name,
            ProjectFuncionalitiesCounter funcionalitiesCounter
    ) {
        return new ProjectModulesContainer(id, name, funcionalitiesCounter, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public static ProjectModulesContainer fromPrimitives(ProjectModulesContainerDTO data) {
        HashMap<String, Team> teamList = new HashMap<>();
        HashMap<String, Card> cardList = new HashMap<>();
        HashMap<Integer, Module> moduleList = new HashMap<>();

        data.teamsList().forEach((key, value) -> {
            teamList.put(key, Team.fromPrimitives(value));
        });
        data.cardList().forEach((key, value) -> {
            cardList.put(key, Card.fromPrimitives(value));
        });
        data.moduleList().forEach((key, value) -> {
            moduleList.put(key, Module.fromPrimitives(value));
        });

        return new ProjectModulesContainer(
                new ProjectId(data.id()),
                new ProjectName(data.name()),
                new ProjectFuncionalitiesCounter(data.funcionalitiesCounter()),
                teamList,
                cardList,
                moduleList
        );
    }

    public ProjectModulesContainerDTO toPrimitives() {
        HashMap<String, TeamDTO> teamList = new HashMap<>();
        HashMap<String, CardDTO> cardList = new HashMap<>();
        HashMap<Integer, ModuleDTO> moduleList = new HashMap<>();

        this.getTeamList().forEach((key, value) -> {
            teamList.put(key, value.toPrimitives());
        });
        this.getCardList().forEach((key, value) -> {
            cardList.put(key, value.toPrimitives());
        });
        this.moduleList.forEach((key, value) -> {
            moduleList.put(key, value.toPrimitives());
        });

        return new ProjectModulesContainerDTO(
                this.getId().value(),
                this.getName().value(),
                this.getFuncionalitiesCounter().value(),
                this.state.value(),
                teamList,
                cardList,
                moduleList
        );
    }

    public void addModule(Module module) {

        this.moduleList.forEach((key, value) -> {
            if (module.getName().value().equals(value.getName().value())||module.getId().value().equals(value.getId().value()))
                throw new ProjectModuleAlreadyExistsException("El modulo ya existe");
        });

        this.moduleList.put(module.getId().value(), module);
        this.modulesCounter = this.incrementModulesCounter();
    }

    public void addFuncionality(ModuleId moduleId, Funcionality funcionality) {
        Module module = this.moduleList.get(moduleId.value());
        if (module == null)
            throw new ModuleNotFoundException();
        module.addFuncionality(funcionality);
        this.incrementFuncionalitiesCounter();
    }

    private ProjectModulesCounter incrementModulesCounter() {
        return new ProjectModulesCounter(this.modulesCounter.value() + 1);
    }

    // private ProjectModulesCounter decrementModulesCounter() {
    //     if (this.modulesCounter.value() == 0)
    //         throw new ProjectInternalException("Contador de modulos no puede ser menor a cero");
    //     return new ProjectModulesCounter(this.modulesCounter.value() - 1);
    // }


    public HashMap<Integer, Module> getModuleList() {
        return moduleList;
    }

    public ProjectModulesCounter getModulesCounter() {
        return modulesCounter;
    }

    public ProjectState getState() {
        return state;
    }
}
