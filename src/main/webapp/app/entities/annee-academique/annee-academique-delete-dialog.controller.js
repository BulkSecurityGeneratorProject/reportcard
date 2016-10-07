(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('AnneeAcademiqueDeleteController',AnneeAcademiqueDeleteController);

    AnneeAcademiqueDeleteController.$inject = ['$uibModalInstance', 'entity', 'AnneeAcademique'];

    function AnneeAcademiqueDeleteController($uibModalInstance, entity, AnneeAcademique) {
        var vm = this;

        vm.anneeAcademique = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AnneeAcademique.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
