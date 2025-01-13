package com.casper.sdk.model.contract.entrypoint;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * Only users from the listed groups may call this method. Note: if the list is empty then this method is not callable
 * from outside the contract.
 *
 * @author ian@meywood.com
 */
@JsonTypeName("Group")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupsAccess implements EntryPointAccess, Iterable<String> {
    private List<String> groups;

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return groups.iterator();
    }
}
