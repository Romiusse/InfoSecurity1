package rmsse.infosecurity1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rmsse.infosecurity1.entities.DataItem;

import java.util.List;

@Repository
public interface DataItemRepository extends JpaRepository<DataItem, Long> {

    @Query("SELECT d FROM DataItem d JOIN d.user u ORDER BY d.createdAt DESC")
    List<DataItem> findAllWithUser();

    @Query("SELECT d FROM DataItem d WHERE d.user.username = :username ORDER BY d.createdAt DESC")
    List<DataItem> findByUsername(@Param("username") String username);

    @Query("SELECT d FROM DataItem d WHERE d.user.id = :userId ORDER BY d.createdAt DESC")
    List<DataItem> findByUserId(@Param("userId") Long userId);
}