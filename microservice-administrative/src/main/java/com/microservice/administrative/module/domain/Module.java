package com.microservice.administrative.module.domain;

import com.microservice.administrative.funcionality.domain.Funcionality;
import com.microservice.administrative.funcionality.domain.FuncionalityDTO;
import com.microservice.administrative.funcionality.domain.ModuleFuncionalityAlreadyExists;
import com.microservice.administrative.shared.domain.ModuleId;

import java.util.HashMap;

public final class Module {
    private final ModuleId id;
    private final ModuleName name;
    private final ModuleCreationDate creationDate;
    private final HashMap<Integer, Funcionality> funcionalityList;
    private ModuleFuncionalitiesCounter funcionalitiesCounter;

    public Module(
            ModuleId id,
            ModuleName name,
            ModuleCreationDate creationDate,
            HashMap<Integer, Funcionality> funcionalityList
    ) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.funcionalitiesCounter = new ModuleFuncionalitiesCounter(funcionalityList.size());
        this.funcionalityList = funcionalityList;
    }

    public static Module fromPrimitives(ModuleDTO data) {
        HashMap<Integer, Funcionality> funcionalityList = new HashMap<>();
        data.funcionalityList().forEach((key, value) -> {
            funcionalityList.put(key, Funcionality.fromPrimitives(value));
        });

        return new Module(
                new ModuleId(data.id()),
                new ModuleName(data.name()),
                new ModuleCreationDate(data.creationDate()),
                funcionalityList
        );
    }

    public ModuleDTO toPrimitives() {
        HashMap<Integer, FuncionalityDTO> funcionalityList = new HashMap<>();
        this.funcionalityList.forEach((key, value) -> {
            funcionalityList.put(key, value.toPrimitives());
        });
        return new ModuleDTO(
                this.id.value(),
                this.name.value(),
                this.creationDate.value(),
                this.funcionalitiesCounter.value(),
                funcionalityList
        );
    }

    public void addFuncionality(Funcionality funcionality) {
        if (this.funcionalityList.containsValue(funcionality))
            throw new ModuleFuncionalityAlreadyExists("La funcionalidad ya existe");
        this.funcionalityList.put(funcionality.getId().value(), funcionality);
        this.funcionalitiesCounter = this.incrementFuncionalityCounter();
    }

    private ModuleFuncionalitiesCounter incrementFuncionalityCounter() {
        return new ModuleFuncionalitiesCounter(this.funcionalitiesCounter.value() + 1);
    }

    public ModuleId getId() {
        return this.id;
    }

    public ModuleName getName() {
        return this.name;
    }

    public ModuleCreationDate getCreationDate() {
        return this.creationDate;
    }

    public ModuleFuncionalitiesCounter getFuncionalitiesCounter() {
        return this.funcionalitiesCounter;
    }

    public HashMap<Integer, Funcionality> getFuncionalityList() {
        return this.funcionalityList;
    }
}
